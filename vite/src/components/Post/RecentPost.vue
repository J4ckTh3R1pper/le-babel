<template>
    <suspense>
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
    </suspense>
</template>

<script setup>
import useRecentPostStore from '@/js/module/recent_post';
import PostMinimal from './PostMinimal.vue';
import { storeToRefs } from 'pinia';
import { getPostMinimalList } from '@/js/api/post';

const recentPostStore = useRecentPostStore()
const {list} = storeToRefs(recentPostStore)
const dataList = ref([])

watch(list, async () => {
    dataList.value = await getPostMinimalList(list.value)
    console.log(dataList.value)
}, {immediate: true})

</script>

<style lang="scss" scoped>

</style>