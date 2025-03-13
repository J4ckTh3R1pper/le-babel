// 初始化markdown-it
import MarkdownIt from "markdown-it";
import MarkdownItHighlightJS from "markdown-it-highlightjs";

const md = new MarkdownIt();
md.use(MarkdownItHighlightJS);

const btn_replacement = '<pre><button class="cpy-btn"><i class="fa-regular fa-clipboard fa-fw"></i><span class="cpy-tooltip">复制代码</span></button>';

//定义默认渲染函数
const proxy = (tokens, idx, options, env, self) => self.renderToken(tokens, idx, options);

// 重写fence型代码块的渲染函数
const defaultFenceRenderer = md.renderer.rules.fence || proxy;
md.renderer.rules.fence = function(tokens, idx, options, env, self) {
    const html = defaultFenceRenderer(tokens, idx, options, env, self);
    return html.replace('<pre>', btn_replacement);
}

// 重写代码块渲染函数
const defaultCodeBlockRenderer = md.renderer.rules.code_block || proxy;
md.renderer.rules.code_block = function(tokens, idx, options, env, self) {
    const html = defaultCodeBlockRenderer(tokens, idx, options, env, self);
    return html.replace('<pre>', btn_replacement);
}
export default md;