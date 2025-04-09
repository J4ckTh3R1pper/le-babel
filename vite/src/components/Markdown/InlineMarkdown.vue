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

const result = computed(() => {
    return md.renderInline(mdText)
})

</script>

<style lang="scss" scoped>

</style>