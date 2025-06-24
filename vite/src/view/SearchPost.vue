<template>
    <div class="seach-post">
        <h1>{{ '共' + resultCount +'条结果' }}</h1>
        <PostBriefView v-for="i in list"
            :post-id="i.id"
            :title="i.title"
            :user-id="i.userId"
            :category-id="i.categoryId"
            :content="i.content"
            :create-time="i.createTime"
            :last-update-time="i.lastUpdateTime"
            :tags="i.tags"
            :view-count="i.viewCount"
            :like-count="i.likeCount"
            :comment-count="i.commentCount"
            :thumbnails="i.thumbnails"
            :liked="i.liked"
            show-category
            show-user
            :key="i.id"
    />

    </div>
    </template>

<script setup>
import PostBriefView from '@/components/Post/PostBriefView.vue';
import PostList from '@/components/Post/PostList.vue';
import { getSingle, searchPost } from '@/js/api/post';
import { isEmpty, isNumber, result } from 'lodash';
import { nextTick } from 'vue';
import { useRoute } from 'vue-router';
const route = useRoute()
const keyword = ref(route.params['keyword'])
const emit = defineEmits(['finish-load'])
const resultCount = ref(0)

watch(route, () => {
    keyword.value = route.params['keyword']
})

const list = ref([])

watch(keyword, async newWord => {
    try {
        if (isEmpty(newWord)) throw new Error('搜索关键词为空!')
        let ids = await searchPost(newWord)
        resultCount.value = ids.length
        list.value = []
        ids.forEach(async id => {
            let view = await getPostBriefView(id)
            list.value.push(view)
        });
    } catch (err) {
        ElMessage({message: err, type: 'error'})
    } finally {
        emit('finish-load')
    }
    
}, {immediate: true})

async function getPostBriefView(id) {
    return await getSingle(id)
}

</script>

<style scoped>

</style>