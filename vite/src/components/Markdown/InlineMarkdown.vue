<template>
    <span v-html="result"></span>
</template>

<script setup>
import useMarkdownStore from '@/js/module/markdown';
import { computed } from 'vue';


const {mdText} = defineProps({
    mdText: String
})

const md = useMarkdownStore()
const instance = md.instance

const result = ref('')

watch(() => mdText, () => {
    result.value = instance.renderInline(mdText)
}, {immediate: true})

</script>

<style lang="scss" scoped>

</style>