<script setup>
import UserBriefView from "@/components/User/UserBriefView.vue";
import {ChatDotSquare, Clock, View} from "@element-plus/icons-vue";
import Time from "@/components/Time.vue";
import PostData from "./PostData.vue";

const {
  postId, title, userId, categoryId, content, createTime, lastUpdateTime, tags, viewCount, likeCount, commentCount, thumbnails, liked
} = defineProps({
  postId: Number,
  title: String,
  content: String,
  createTime: Number,
  lastUpdateTime: Number,
  viewCount: Number,
  likeCount: Number,
  liked: Boolean,
  commentCount: Number,
  thumbnails: {
    type: Array,
    default: () => []
  },
  tags: {
    type: Array,
    default: () => []
  },
})


</script>

<template>
  <div class="post-brief-view">
    <UserBriefView
      :user-id="userId"
      :category-id="categoryId"
    />
  </div>
  <el-link :href="`/postDetail?id=${postId}`">{{title}}</el-link>
  <span class="content">{{content}}</span>
  <div v-if="thumbnails.length > 0" class="thumbnails">
    <el-image v-for="url in thumbnails" :src="'/api' + url" />
  </div>
  <PostData
    :post-id="postId"
    :view-count="viewCount"
    :comment-count="commentCount"
    :like-count="likeCount"
    :liked="liked"
  />
  <span class="createTime">
    <el-icon><Clock /></el-icon>
    <Time :timestamp="createTime"/>
  </span>
</template>

<style scoped lang="scss">
  .post-brief-view {
    display: flex;
    flex-direction: column;
  }
  .thumbnails {
    display: flex;
    flex-direction: row;
  }
</style>