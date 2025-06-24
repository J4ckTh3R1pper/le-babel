<template>
    <el-card>
        <template #header>
            <div class="header">最近帖子</div>
        </template>
        <PostMinimal v-for="i in dataList"
                     :postId="i.id"
                     :category-id="i.categoryId"
                     :title="i.title"
                     :thumbnail="i.thumbnail"
                     :like-count="i.likeCount"
                     :comment-count="i.commentCount"
                     :key="i.id"
        />
    </el-card>
</template>

<script setup>
import useRecentPostStore from '@/js/module/recent_post';
import PostMinimal from './PostMinimal.vue';
import { storeToRefs } from 'pinia';
import { getPostMinimalList } from '@/js/api/post';
import {isEmpty} from "lodash";

const recentPostStore = useRecentPostStore()
const {postList} = storeToRefs(recentPostStore)
const dataList = ref([])

watch(postList, async () => {
    if (!isEmpty(postList.value)) dataList.value = await getPostMinimalList(postList.value)
    // console.log(dataList.value)
}, {immediate: true})

</script>

<style lang="scss" scoped>

</style>