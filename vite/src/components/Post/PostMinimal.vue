<template>
    <div class="post-minimal-view">
        <div class="left">
            <div class="category-info">
                <el-avatar :src="categoryData.avatar" size="small"/><el-link class="name">{{ categoryData.name }}</el-link><br/>
            </div>
            <div class="title">
                <el-link class="name">{{ title }}</el-link><br/>
            </div>
            <div class="data">
                <el-text>{{ '点赞: ' + likeCount }}</el-text>
                <el-divider direction="vertical"/>
                <el-text>{{ '评论: ' + commentCount }}</el-text>
            </div>
        </div>
        <el-image v-if="!isEmpty(thumbnail)" :src="thumbnail"/>
    </div>
    <el-divider/>
</template>

<script setup>
import useCategoryCacheStore from '@/js/module/category_cache';
import { isEmpty } from 'lodash';

const {postId, categoryId, title, likeCount, commentCount, thumbnail} = defineProps({
    postId: {
        type: Number,
        required: true
    },
    categoryId: {
        type: Number,
        required: true
    },
    title: {
        type: String,
        required: true
    },
    likeCount: Number,
    commentCount: Number,
    thumbnail: String
})

const categoryCache = useCategoryCacheStore()
const categoryData = await categoryCache.fetchCategory(categoryId)

</script>

<style lang="scss" scoped>
.post-minimal-view {
    display: flex;
    flex-direction: column;
    .left > * {
        &:nth-child(n) {
            margin-bottom: 8px;
        }
    }
}
.category-info {
    display: flex;
    align-items: center;
}
.title {
    display: flex;
    .name {
        flex-wrap: wrap;
    }
}
.el-avatar {
    margin-right: 6px;
}
</style>