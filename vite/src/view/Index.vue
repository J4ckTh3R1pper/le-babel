<template>
    <PostList :size="15" show-category @finish-load="emit('finish-load')"/>
</template>

<script setup>
import PostList from '@/components/Post/PostList.vue';
import { useRoute } from 'vue-router';

const emit = defineEmits(['finish-load'])
const route = useRoute()
watch (route, () => {
    emit('finish-load')
})

</script>

<style lang="scss" scoped>
</style>