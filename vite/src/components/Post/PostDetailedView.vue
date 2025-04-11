<script setup>

import {ChatDotSquare, Clock, Star, StarFilled, View} from "@element-plus/icons-vue";
import UserBriefView from "@/components/User/UserBriefView.vue";
import Time from "@/components/Time.vue";
import Markdown from "@/components/Markdown/index.vue"
import PostData from "./PostData.vue";

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
    <div class="top">
      <UserBriefView :user-id="userId" :category-id="categoryId" full/>
      <el-divider direction="vertical"/>
      <Time :timestamp="createTime" class="time"/>
    </div>
    <div class="title">{{title}}</div>
    <div class="content"><Markdown :md-text="content"/></div>
    <div class="bottom">
      <PostData
        :post-id="postId"
        :view-count="viewCount"
        :comment-count="commentCount"
        :like-count="likeCount"
        :liked="liked"
      />
    </div>
  </div>
</template>

<style scoped lang="scss">
.post-detailed-view {
    display: flex;
    flex-direction: column;
    padding: 1em;
    .bottom,.top {
      display: flex;
      flex-direction: row;
      align-items: center;
    }
    .time,.category-name {
      font-size: small;
      color: #646464;
    }
    > * {
      &:nth-child(n+2) {
        padding-top: 8px;
        &:not(.data) {
          padding-left: 8px;
        }
      }
    }
  }

.data {
  display: flex;
  flex-direction: row;
  align-items: baseline;
}

</style>