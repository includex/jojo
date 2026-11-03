"""Author-time converter for the bounded syntax emitted by build_linchong_mod.

The resulting Lua is source content executed by LuaJ, never converted back to AST.
Unknown syntax fails rather than silently changing a scenario.
"""
from __future__ import annotations
import ast
import json


class Converter:
    def __init__(self):
        self.locals = set()

    def truth(self, node):
        value = self.expr(node)
        if isinstance(node, ast.Subscript) and isinstance(node.value, ast.Name) and node.value.id in ('gvars', 'vars'):
            return f'({value} ~= 0 and {value} ~= nil and {value} ~= false)'
        if isinstance(node, ast.Constant):
            return 'true' if bool(node.value) else 'false'
        if isinstance(node, (ast.BoolOp, ast.Compare)) or (isinstance(node, ast.UnaryOp) and isinstance(node.op, ast.Not)):
            return value
        return '__truth(' + value + ')'

    def expr(self, node):
        if isinstance(node, ast.Constant):
            if node.value is None: return 'nil'
            if node.value is True: return 'true'
            if node.value is False: return 'false'
            if isinstance(node.value, str): return json.dumps(node.value, ensure_ascii=False)
            return str(node.value)
        if isinstance(node, ast.Name): return node.id
        if isinstance(node, ast.Attribute):
            if node.attr in {'and','break','do','else','elseif','end','false','for','function','goto','if','in','local','nil','not','or','repeat','return','then','true','until','while'}:
                return self.expr(node.value) + '[' + json.dumps(node.attr) + ']'
            return self.expr(node.value) + '.' + node.attr
        if isinstance(node, ast.Subscript): return self.expr(node.value) + '[' + self.expr(node.slice) + ']'
        if isinstance(node, ast.Call):
            if node.keywords: raise ValueError('Keyword arguments require an explicit Lua API mapping')
            return self.expr(node.func) + '(' + ', '.join(self.expr(a) for a in node.args) + ')'
        if isinstance(node, ast.List): return '{' + ', '.join(self.expr(a) for a in node.elts) + '}'
        if isinstance(node, ast.Dict):
            return '{' + ', '.join('[' + self.expr(k) + '] = ' + self.expr(v) for k,v in zip(node.keys,node.values)) + '}'
        if isinstance(node, ast.UnaryOp):
            if isinstance(node.op, ast.Not): return '(not ' + self.truth(node.operand) + ')'
            if isinstance(node.op, ast.USub): return '-' + self.expr(node.operand)
        if isinstance(node, ast.BoolOp):
            operator = ' and ' if isinstance(node.op, ast.And) else ' or '
            return '(' + operator.join(self.truth(v) for v in node.values) + ')'
        if isinstance(node, ast.Compare):
            ops = {ast.Eq:'==',ast.NotEq:'~=',ast.Gt:'>',ast.GtE:'>=',ast.Lt:'<',ast.LtE:'<='}
            operands=[node.left]+node.comparators
            return '(' + ' and '.join(self.expr(a)+' '+ops[type(op)]+' '+self.expr(b) for a,op,b in zip(operands,node.ops,operands[1:])) + ')'
        if isinstance(node, ast.BinOp) and isinstance(node.op, ast.Add): return '('+self.expr(node.left)+' + '+self.expr(node.right)+')'
        raise ValueError('Unsupported scenario expression: ' + ast.dump(node))

    def block(self, nodes, depth=0):
        result=[]
        pad='    '*depth
        for node in nodes:
            if isinstance(node, ast.FunctionDef):
                if node.args.args: raise ValueError('Scenario functions currently take no parameters')
                self.locals=set()
                result += [pad+'function '+node.name+'()', *self.block(node.body,depth+1),pad+'end','']
            elif isinstance(node, ast.If):
                result += [pad+'if '+self.truth(node.test)+' then',*self.block(node.body,depth+1)]
                if node.orelse: result += [pad+'else',*self.block(node.orelse,depth+1)]
                result += [pad+'end']
            elif isinstance(node, ast.Assign):
                if len(node.targets)!=1: raise ValueError('Multiple assignment targets unsupported')
                target=node.targets[0];prefix=''
                if isinstance(target,ast.Name) and target.id not in self.locals:
                    prefix='local ';self.locals.add(target.id)
                result.append(pad+prefix+self.expr(target)+' = '+self.expr(node.value))
            elif isinstance(node, ast.AugAssign) and isinstance(node.op, ast.Add):
                target=self.expr(node.target);result.append(pad+target+' = '+target+' + '+self.expr(node.value))
            elif isinstance(node, ast.Expr):
                if isinstance(node.value,ast.Call) and isinstance(node.value.func,ast.Name) and node.value.func.id=='label': continue
                result.append(pad+self.expr(node.value))
            elif isinstance(node, ast.Return): result.append(pad+'return '+(self.expr(node.value) if node.value else 'nil'))
            elif isinstance(node, ast.Pass): pass
            else: raise ValueError('Unsupported scenario statement: ' + ast.dump(node))
        return result


def convert(source: str) -> str:
    return '-- Generated Linchong scenario. Edit the campaign generator to preserve changes.\nlocal function __truth(value)\n    return value ~= nil and value ~= false and value ~= 0 and value ~= \"\"\nend\n\n' + '\n'.join(Converter().block(ast.parse(source).body)).rstrip()+'\n'
