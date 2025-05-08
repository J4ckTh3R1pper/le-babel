<template>
    <div class="user-profile">
        <div class="header">
            <span class="left">
                <el-avatar :src="isEmpty(data.headImgUrl) ? defaultAvatar : data.headImgUrl" :size="64"/>
                <el-text class="nickname">{{ data.nickName }}</el-text>
            </span>
            <span class="right">
                <el-button @click="router.push({name: 'user_edit'})" v-if="userId == loginUserStore.userId" type="primary">编辑个人资料</el-button>
            </span>
        </div>
        <PostList :user-id="userId"
            @finish-load="emit('finish-load')"
            v-if="isNumber(userId)"
        />
    </div>
</template>

<script setup>
import PostList from '@/components/Post/PostList.vue';
import useUserCacheStore from '@/js/module/user_cache';
import { isEmpty, isNumber } from 'lodash';
import { useRoute, useRouter } from 'vue-router';
import defaultAvatar from '@/assets/images/default_user_avatar.png'
import useLoginUserStore from '@/js/module/login_user';

const route = useRoute()
const router = useRouter()
const userId = ref(Number(route.params['id']))
const emit = defineEmits(['finish-load'])
const userCacheStore = useUserCacheStore()
const loginUserStore = useLoginUserStore()
const data = ref(await userCacheStore.fetchUser(userId.value, true))

watch(route, () => {
    if (route.name == 'user_profile') userId.value = Number(route.params['id'])
})

watch(userId, async () => {
    data.value = await userCacheStore.fetchUser(userId.value, true)
})

</script>

<style lang="scss" scoped>
.header {
    display: flex;
    margin: 1em 0;
    justify-content: space-between;
    .left, .right {
        display: flex;
        align-items: center;
        .el-avatar {
            margin-right: 12px;
        }
        .nickname {
            font-size: 24px;
        }
    }
}
</style>