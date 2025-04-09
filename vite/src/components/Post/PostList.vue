<template>
<el-select v-model="sort">
    <el-option v-for="i in sortings"
        :value="i.value"
        :label="i.label"
        :key="i.label"
    ></el-option>
</el-select>
<div class="list">
    <ul v-infinite-scroll="load">
        <li v-for="i in list">
            <PostBriefView
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
            />
        </li>
        <li v-show="loading">
            <el-icon><Loading /></el-icon>
        </li>
    </ul>
</div>
</template>

<script setup>
import { getPostSlice } from '@/js/api/post';
import PostBriefView from './PostBriefView.vue';
const sort = ref(sortings[0])
const sortings = [
    {
        label: '最新回复',
        value: 'lastUpdateTime'

    },
    {
        label: '最新发布',
        value: 'createTime'
    },
    {
        label: '最多回复',
        value: 'commentCount'
    },

]
const page = ref(1)
const {categoryId, size} = defineProps({
    categoryId: Number,
    size: Number,
})
const list = ref([])
const loading = ref(false)

async function load() {
    loading.value = true
    getPostSlice(categoryId, page.value, size, sort.value+',desc').then(res => {
        if (!res["empty"])
            list.value = list.value.concat(slice.content)
        page += 1
    }).finally( () => {
        loading.value = false
    })
}

</script>

<style scoped lang="scss">
</style>