<script setup>

import {ChatDotSquare, Clock, Star, StarFilled, View} from "@element-plus/icons-vue";
import UserBriefView from "@/components/User/UserBriefView.vue";
import Time from "@/components/Time.vue";
import Markdown from "@/components/Markdown/index.vue"

const {
  postId, categoryId, title, userId, content, createTime, lastUpdateTime, tags, viewCount, likeCount, commentCount, liked
} = defineProps({
  postId: Number,
  categoryId: Number,
  title: String,
  userId: Number,
  content: String,
  createTime: Number,
  lastUpdateTime: Number,
  viewCount: Number,
  likeCount: Number,
  commentCount: Number,
  liked: Boolean,
  tags: {
    type: Array,
    default: () => []
  },
})

</script>

<template>
  <div class="post-detailed-view">
    <user-brief-view
      :user-id="userId"
      :category-id="categoryId"
    />
  </div>
  <span>{{title}}</span>
  <Markdown :md-text="content"/>
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
.post-detailed-view {
  display: flex;
  flex-direction: column;
}
.data {
  display: flex;
  flex-direction: row;
  align-items: baseline;
}

</style>