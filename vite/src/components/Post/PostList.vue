<template>
<el-select v-model="sort">
    <el-option v-for="i in sortings"
        :value="i.value"
        :label="i.label"
        :key="i.label"
    ></el-option>
</el-select>
<div class="post-list">
    <ul v-infinite-scroll="load" 
        :infinite-scroll-delay="500"
        :infinite-scroll-immediate="false"
        :infinite-scroll-disabled="disabled"
        class="list"
    >
        <li v-for="i in list">
            <Suspense>
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
            </Suspense>
            <el-divider/>
        </li>
        <li>
            <div class="loading" v-loading="loading">
                <span class="empty">已经到底了</span>
            </div>
        </li>
    </ul>
</div>
</template>

<script setup>
import { getPostSlice } from '@/js/api/post';
import PostBriefView from './PostBriefView.vue';
import { onMounted, watchEffect } from 'vue';
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
const sort = ref(sortings[0].value)
const page = ref(1)
const {categoryId, size} = defineProps({
    categoryId: Number,
    size: Number,
})
const disabled = ref(false)
const list = ref([])
const loading = ref(false)
const lastPage = ref(false)

watch(sort, async () => {
    list.value = []
    page.value = 1
    load()
}, { immediate: true })


function load() {
    loading.value = true
    getPostSlice(categoryId, page.value, size, sort.value+',desc').then(res => {
        let slice = res
        if (!slice["empty"]) {
            list.value = list.value.concat(slice.content)
            page.value += 1
            lastPage.value = false
        } else lastPage.value = true
    }).catch(() => {
        disabled.value = true
    })
    .finally( () => {
        loading.value = false
    })
}

</script>

<style scoped lang="scss">
.post-list {
    height: 100%;
    overflow: hidden;
}
.list {
    height: 100%;
    overflow-y: scroll;
    list-style: none;
    padding: 0;
}

.loading {
    height: 5em;
    display: flex;
    justify-content: center;
    .empty {
        color: gray;
        padding-top: 1em;
    }
}
</style>