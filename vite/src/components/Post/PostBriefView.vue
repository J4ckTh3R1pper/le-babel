<script setup>
import UserBriefView from "@/components/User/UserBriefView.vue";
import {ChatDotSquare, Clock, View} from "@element-plus/icons-vue";
import Time from "@/components/Time.vue";
import PostData from "./PostData.vue";
import InlineMarkdown from "../Markdown/InlineMarkdown.vue";
import useUserCacheStore from "@/js/module/user_cache";
import useCategoryCacheStore from "@/js/module/category_cache";
import { storeToRefs } from "pinia";
import { isEmpty } from "lodash";
import { useRoute, useRouter } from "vue-router";

const {
  postId, title, userId, categoryId, showCategory = true, content, createTime, lastUpdateTime, tags, viewCount, likeCount, commentCount, thumbnails, liked, showUser = true
} = defineProps({
  postId: Number,
  title: String,
  userId: Number,
  categoryId: Number,
  showCategory: Boolean,
  showUser: Boolean,
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

const router = useRouter()
const route = useRoute()

const userCacheStore = useUserCacheStore()

const categoryStore = useCategoryCacheStore()
const {getCategory} = storeToRefs(categoryStore)

function onClicked() {
  router.push('/postDetail/' + postId)
}
let categoryName= ''

if (showCategory) {
  categoryName = (await categoryStore.fetchCategory(categoryId))['name']
}

window.translate.execute();
</script>

<template>
  <div class="post-brief-view" @click="onClicked">
    <div class="top">
      <template v-if="showUser">
        <UserBriefView :user-id="userId" :category-id="categoryId"/>
        <el-divider direction="vertical"/>
      </template>
      <Time :timestamp="createTime" class="time"/>
      <!-- https://stackoverflow.com/questions/77397035/pinia-getter-undefined-if-used-with-filter -->
      <template v-if="!isEmpty(categoryName)">
        <el-divider direction="vertical"/>
        <span class="category-name">{{ categoryName }}</span>
      </template>
    </div>
    <div>
      <el-link class="post-link" type="primary" @click.self.prevent>{{title}}</el-link>
    </div>
    <div class="content">
      <InlineMarkdown :md-text="content"/>
    </div>
    <div v-if="thumbnails.length > 0" class="thumbnails">
      <el-image v-for="url in thumbnails" :src="url" />
    </div>
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
  <el-divider/>
</template>

<style scoped lang="scss">
  .post-brief-view {
    display: flex;
    flex-direction: column;
    cursor: pointer;
    border-radius: 8px;
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
        &:not(.bottom) {
          padding-left: 8px;
        }
      }
    }
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