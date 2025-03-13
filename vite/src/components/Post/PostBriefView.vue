<script setup>
import UserBriefView from "@/components/User/UserBriefView.vue";
import {ChatDotSquare, Clock, View} from "@element-plus/icons-vue";
import Time from "@/components/Time.vue";

const {
  id, title, userBriefView, content, createTime, lastUpdateTime, tagList, views, likeCount, commentCount, thumbnails
} = defineProps({
  id: Number,
  title: String,
  userBriefView: {
    type: Object,
    default: () => {}
  },
  content: String,
  createTime: Number,
  lastUpdateTime: Number,
  tagList: {
    type: Array,
    default: () => []
  },
  views: Number,
  likeCount: Number,
  commentCount: Number,
  thumbnails: {
    type: Array,
    default: () => []
  }
})


</script>

<template>
  <div class="post-brief-view">
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
  <el-link :href="`/postDetail?id=${id}`">{{title}}</el-link>
  <span class="content">{{content}}</span>
  <div v-if="thumbnails.length > 0" class="thumbnails">
    <el-image v-for="url in thumbnails" :src="'/api' + url" />
  </div>
  <div class="data">
    <span class="views">
      <el-icon><View /></el-icon>
      {{views}}
    </span>
    <span class="comments">
      <el-icon><ChatDotSquare /></el-icon>
      {{commentCount}}
    </span>
    <span class="createTime">
      <el-icon><Clock /></el-icon>
      <Time :timestamp="createTime"/>
    </span>
  </div>
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
  .data {
    display: flex;
    flex-direction: row;
  }
</style>