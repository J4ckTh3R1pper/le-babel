<script setup>
import UserBriefView from "@/components/User/UserBriefView.vue";
import {ChatDotSquare, Clock, View} from "@element-plus/icons-vue";
import Time from "@/components/Time.vue";
import PostData from "./PostData.vue";
import InlineMarkdown from "../Markdown/InlineMarkdown.vue";

const {
  postId, title, userId, categoryId, content, createTime, lastUpdateTime, tags, viewCount, likeCount, commentCount, thumbnails, liked
} = defineProps({
  postId: Number,
  title: String,
  userId: Number,
  categoryId: Number,
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
    <span>
    <el-link class="post-link" type="primary" :href="`/postDetail?id=${postId}`">{{title}}</el-link>
    </span>
    <div class="content">
      <InlineMarkdown :md-text="content"/>
    </div>
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
      <Time :timestamp="createTime * 1000"/>
    </span>
  </div>
</template>

<style scoped lang="scss">
  .post-brief-view {
    display: flex;
    flex-direction: column;
    cursor: pointer;
    border-radius: 8px;
    // justify-content: start;
    // align-items: start;
    padding: 1em;
    > * {
      &:nth-child(n+2) {
        padding-top: 4px;
      }
    }
    // &:nth-child(n) {
    //   border-bottom: #c4c4c4 1px solid;
    // }
    &:hover {
      background-color: rgba(0, 0, 0, 0.09);
    }
  }

  .post-link {
    font-size: 18px;
    flex-grow: 0;
    justify-content: start;
  }
  .content {
    color: #646464;
  }
  .thumbnails {
    display: flex;
    flex-direction: row;
  }
</style>