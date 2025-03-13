import md from "@/js/utils/markdown.js";
import {defineStore} from "pinia";

const useMarkdownStore =  defineStore(
    'markdown',
    {
        state: () => ({
            instance: md
        })
    }
)

export default useMarkdownStore