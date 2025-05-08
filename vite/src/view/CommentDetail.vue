<template>
    <div class="commentDetail">
        <CommentBriefView :data="data" detailed/>
    </div>
</template>

<script setup>
import CommentBriefView from '@/components/Comment/CommentBriefView.vue';
import { getCommentDetail } from '@/js/api/comment';
import { useRoute } from 'vue-router';
const route = useRoute()
const emit = defineEmits(['finish-load', 'category-change'])

const commentId = ref(Number(route.params['id']))
const data = ref(await getCommentDetail(commentId.value))
emit('finish-load')
emit('category-change', data.value['categoryId'])

watch(route, () => {
    commentId.value = Number(route.params['id'])
})

watch (commentId, async (newId) => {
    data.value = await getCommentDetail(newId)
    emit('finish-load')
    emit('category-change', data.value['categoryId'])
})


</script>

<style lang="scss" scoped>

</style>