<template>
  <div class="data">
    <span class="left">
      <span class="views">
        <el-icon><View /></el-icon>{{viewCount}}
      </span>
      <span class="comments">
        <el-icon><ChatDotSquare /></el-icon>{{commentCount}}
      </span>
      <span class="like" :class="{'liked': liked}" @click="like">
        <el-icon><StarFilled v-if="liked"/><Star v-else/></el-icon>{{likeCount}}
      </span>
    </span>
    <span class="right">

    </span>
  </div>
</template>

<script setup>

import {likePost} from '@/js/api/post.js'


const props = defineProps({
    viewCount: Number,
    commentCount: Number,
    likeCount: Number,
    liked: Boolean,
    postId: Number
})

const viewCount = ref(props["viewCount"])
const commentCount = ref(props["commentCount"])
const likeCount = ref(props["likeCount"])
const liked = ref(props["liked"])
const postId = props["postId"]

function like() {
  likePost(postId).then(res => {
    liked.value = res.liked
    likeCount.value = res.count
  })
}
</script>

<style scoped lang="scss">
.data {
    display: flex;
    flex-direction: row;
    align-items: baseline;
    // justify-content: space-around;
    .left{
      display: flex;
      flex-direction: inherit;
      align-items: inherit;
      & > * {
        &:nth-child(n) {
          display: flex;
          border-radius: 16px;
          padding: 8px;

          align-items: center;
          justify-content: space-between;
          .el-icon {
            margin-right: 4px;
          }
        }
        &:nth-child(n+2) {
          margin-left: 8px;
        }
      }
      .comments,.like {
        cursor: pointer;
        &:hover {
          background-color: rgba(0, 0, 0, 0.127);
        }
      }
      .liked {
        color: #FFFFFF;
        border-color: rgb(255, 77, 77);
        background-color: rgb(255, 77, 77);
        &:hover {
          color: black;
        }
      }
    }

}
</style>