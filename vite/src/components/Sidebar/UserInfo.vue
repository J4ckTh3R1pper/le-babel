<template>
    <el-card class="user-info">
        <template #header>
            <el-text class="nickname">{{ data.nickName }}</el-text>
        </template>
        <div v-if="isLoggedIn && userId != loginUserStore.userId">
            <el-button :type="followed ? 'default' : 'primary'" @click="follow" >
                {{ followed ? '已关注' : '关注' }}
            </el-button>
        </div>
        <el-text>{{ '个人简介: ' + (isEmpty(data.introduce) ? defaultIntro : data.introduce) }}</el-text>
        <div class="data">
            <el-text>{{ '总经验值: ' + overallExp }}</el-text>
            <el-text>{{ '加入时间: ' + joinDate }}</el-text>
        </div>
        <div class="data">
            <el-text>{{ '关注: ' + data.followingCount }}</el-text>
            <el-text>{{ '粉丝: ' + data.followerCount }}</el-text>
            <el-text>{{ '性别: ' + getGenderText() }}</el-text>
        </div>
    </el-card>
</template>

<script setup>
import { followUser, getFollowed, getFullInfo, getOverallExp } from '@/js/api/user';
import useLoginUserStore from '@/js/module/login_user';
import { isEmpty } from 'lodash';
import { storeToRefs } from 'pinia';

const {userId} = defineProps({
    userId: {
        type: Number,
        required: true
    }
})
const loginUserStore = useLoginUserStore()

const {isLoggedIn} = storeToRefs(loginUserStore)

const defaultIntro = "这个人太懒了，没有写简介~"
const followed = ref(false)
const data = ref(await getFullInfo(userId))
const overallExp = ref(await getOverallExp(userId))
function getGenderText() {
    if (data.value == null) return ''
    switch (data.value['gender']) {
        case 'UNKNOWN': return '保密'
        case 'MALE': return '男'
        case 'FEMALE': return '女'
    }
}

const joinDate = computed(() => {
    let date = new Date(data.value.createTime * 1000)
    return date.toISOString().split('T')[0]
})

getFollowedStatus()

watch(() => userId, async () => {
    data.value = await getFullInfo(userId)
    overallExp.value = await getOverallExp(userId)
    getFollowedStatus()
})

async function getFollowedStatus() {
    if (isLoggedIn.value)
        followed.value = await getFollowed(userId)
}

async function follow() {
    if (isLoggedIn.value) {
        followed.value = await followUser(userId)
        data.value = await getFullInfo(userId)
    }
}


</script>

<style lang="scss" scoped>
.user-info {
    .data {
        display: flex;
        justify-content: space-between;
    }
}
</style>