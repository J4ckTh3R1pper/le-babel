<template>
  <div class="data">
    <span class="views">
      <el-icon><View /></el-icon>
      {{viewCount}}
    </span>
    <span class="comments">
      <el-icon><ChatDotSquare /></el-icon>
      {{commentCount}}
    </span>
    <span class="like" @click="like">
      <el-icon>
        <StarFilled v-if="liked"/>
        <Star v-else/>
      </el-icon>
      {{likeCount}}
    </span>
  </div>
</template>

<script setup>

import likePost from '@/js/api/post.js'


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
    liked.value = res.data
    if (liked.value) likeCount += 1
    else likeCount -= 1
  })
}
</script>

<style scoped lang="scss">
.data {
    display: flex;
    flex-direction: row;
    align-items: baseline;
}
</style>