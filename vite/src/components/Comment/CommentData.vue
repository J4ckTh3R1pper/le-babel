<template>
    <div class="data">
        <span class="left">
            <span class="comments" @click.stop="toggleReplyInput">
                <el-icon><ChatDotSquare /></el-icon>{{commentCount}}
            </span>
            <span class="like" :class="{'liked': liked}" @click.stop="like">
            <el-icon><StarFilled v-if="liked"/><Star v-else/></el-icon>{{likeCount}}
      </span>
        </span>
    </div>
</template>

<script setup>
import { getCommentData, likeComment } from '@/js/api/comment';
import { nextTick } from 'vue';

const props = defineProps({
    commentId: {
        type: Number,
        required: true
    },
    commentCount: Number,
    liked: Boolean,
    likeCount: Number
})

const emit = defineEmits(['toggle-reply-input'])
const liked = ref(props['liked'])
const commentId = ref(props['commentId'])
const commentCount = ref(props['commentCount'])
const likeCount = ref(props['likeCount'])

async function like() {
  await likeComment(commentId.value)
  await refresh()
}

function toggleReplyInput() {
  emit('toggle-reply-input')
}

async function refresh() {
  let data = await getCommentData(commentId.value)
  liked.value = data.liked
  commentCount.value = data.childCount
  likeCount.value = data.likeCount
  await nextTick()
}

defineExpose({
  refresh
})

</script>

<style lang="scss" scoped>
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
          background-color: rgba(0, 0, 0, 0.048);
          align-items: center;
          justify-content: space-between;
          .el-icon {
            margin-right: 8px;
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
          color: rgb(255, 77, 77);
        }
      }
    }
}
</style>