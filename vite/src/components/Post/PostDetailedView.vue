<script setup>

import {ChatDotSquare, Clock, Star, StarFilled, View} from "@element-plus/icons-vue";
import UserBriefView from "@/components/User/UserBriefView.vue";
import Time from "@/components/Time.vue";
import Markdown from "@/components/Markdown/index.vue"

const {
  id, categoryId, title, userBriefView, content, createTime, lastUpdateTime, tags, viewCount, likeCount, commentCount, liked
} = defineProps({
  id: Number,
  categoryId: Number,
  title: String,
  content: String,
  createTime: Number,
  lastUpdateTime: Number,
  viewCount: Number,
  likeCount: Number,
  commentCount: Number,
  liked: Boolean,
  userBriefView: {
    type: Object,
    default: () => {}
  },
  tags: {
    type: Array,
    default: () => []
  },
})

</script>

<template>
  <div class="post-detailed-view">
    <user-brief-view
        :id="userBriefView.id"
        :head-img-url="userBriefView.headImgUrl"
        :role="userBriefView.role"
        :level="userBriefView.level"
        :location="userBriefView.location"
        :nick-name="userBriefView.nickName"
        :title="userBriefView.title"
    />
  </div>
  <span>{{title}}</span>
  <Markdown :md-text="content"/>
  <div class="data">
    <span class="views">
      <el-icon><View /></el-icon>
      {{viewCount}}
    </span>
    <span class="comments">
      <el-icon><ChatDotSquare /></el-icon>
      {{commentCount}}
    </span>
    <span class="like">
      <el-icon>
        <StarFilled v-if="liked"/>
        <Star v-else/>
      </el-icon>
      {{likeCount}}
    </span>
    <span class="createTime">
      <el-icon><Clock /></el-icon>
      <Time :timestamp="createTime"/>
    </span>
  </div>

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