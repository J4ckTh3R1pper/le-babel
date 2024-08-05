<template>
</template>

<script setup>
</script>

<script lang="jsx">
import 'highlight.js/styles/stackoverflow-dark.css'
import {computed, h, ref} from 'vue';
import {Marked} from "marked";
import {markedHighlight} from "marked-highlight";
import hljs from "highlight.js";
import {compile} from "vue";

export default {
    name: "Markdown",
    props: {
        text: String
    },
    setup(props) {
        const marked = ref(new Marked({},
            {   renderer: {
                    code(code, lang, escaped) {
                        console.log(lang);
                        console.log(escaped);
                        return `<markdown-code
                                    code="${ escaped ? code : encodeURI(code) }"
                                    lang="${ lang ? (escaped ? lang : encodeURI(lang)) : "" }"
                                    ${escaped ? "escaped" : ""}
                        ></markdown-code>`
                    }
                }
            }
        ));
        const markdown = computed( () => {
            return compile( marked.value.parse(props.text) )
        });
        return markdown.value
    }
}
</script>

<style lang="scss">
</style>