<script setup>

import {ChatDotSquare, Clock, Star, StarFilled, View} from "@element-plus/icons-vue";
import UserBriefView from "@/components/User/UserBriefView.vue";
import Time from "@/components/Time.vue";
import Markdown from "@/components/Markdown/index.vue"
import PostData from "./PostData.vue";
import { MdPreview } from "md-editor-v3";
import 'md-editor-v3/lib/preview.css';
import useLoginUserStore from "@/js/module/login_user";
import { storeToRefs } from "pinia";
import useUserCacheStore, { defaultMember } from "@/js/module/user_cache";
import { useRouter } from "vue-router";
import { deletePost } from "@/js/api/post";
import { computed } from "vue";

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

const loginUserStore = useLoginUserStore()
const {isLoggedIn} = storeToRefs(loginUserStore)
const userCacheStore = useUserCacheStore()
const router = useRouter()
const getMembership = async () => isLoggedIn.value ? await userCacheStore.fetchMember(categoryId, loginUserStore.userId, true) : defaultMember

const hasDeletePermission = ref(false)

onMounted(async () => {
  if (isLoggedIn.value) hasDeletePermission.value = loginUserStore.userId === userId || await getMembership().role > 1
  else hasDeletePermission.value = false
})

async function onConfirmDelete() {
  try {
    await deletePost(postId)
    router.push({name: 'category_index', params: {id: categoryId}})
  } catch(err) {
    ElMessage({message: err, type: 'error'})
  }
}

</script>

<template>
  <div class="post-detailed-view">
    <div class="top">
      <UserBriefView :user-id="userId" :category-id="categoryId" full/>
      <el-divider direction="vertical"/>
      <Time :timestamp="createTime" class="time"/>
    </div>
    <div class="title">{{title}}</div>
    <!-- <div class="content"><Markdown :md-text="content"/></div> -->
    <MdPreview :model-value="content"/>
    <div class="bottom">
      <PostData class="data"
        :post-id="postId"
        :view-count="viewCount"
        :comment-count="commentCount"
        :like-count="likeCount"
        :liked="liked"
      />
      <el-popconfirm v-if="hasDeletePermission" title="你确定要删除此帖子吗？" @confirm="onConfirmDelete">
        <template #reference  v-if="hasDeletePermission">
          <el-button type="danger"  v-if="hasDeletePermission">
            <el-icon><Delete /></el-icon>
            删除
          </el-button>
        </template>
      </el-popconfirm>
      </div>
  </div>
</template>

<style scoped lang="scss">
.post-detailed-view {
    display: flex;
    flex-direction: column;
    padding: 1em 0;
    .bottom,.top {
      display: flex;
      flex-direction: row;
      align-items: center;
    }
    .title {
      font-size: large;
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
    .bottom {
      display: flex;
      justify-content: space-between;
    }
  }

.data {
  display: flex;
  flex-direction: row;
  align-items: baseline;
}

</style>