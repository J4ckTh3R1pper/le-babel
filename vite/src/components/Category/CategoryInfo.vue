<template>
    <el-card>
        <template #header>
            <el-text>板块详情</el-text>
        </template>
        <el-text>{{ info }}</el-text>
        <el-divider/>
        <template v-if="isNumber(categoryId) && isNumber(userId)">
            <Membership  :category-id="categoryId" :user-id="userId"/>
            <el-divider/>
        </template>
        <template v-if="isEmpty(rule)">
            <el-text >{{ '规则: ' + rule }}</el-text>
            <el-divider/>
        </template>
        <el-text>{{ '创建时间: ' + createDate }}</el-text>
        <el-divider/>
        <el-text>{{ '关注: ' + subscriberCount }}</el-text>
    </el-card>
</template>

<script setup>
import { getCategoryInfo } from '@/js/api/category';
import useRouteHistoryStore from '@/js/module/route_history';
import { find, isEmpty, isNumber, isUndefined } from 'lodash';
import { storeToRefs } from 'pinia';
import useRecentCategoryStore from '@/js/module/recent_category';
import useUserCacheStore from '@/js/module/user_cache';
import useLoginUserStore from '@/js/module/login_user';
import Membership from './Membership.vue';
const route = useRoute()
const historyStore = useRouteHistoryStore()
const recentCategory = useRecentCategoryStore()
const loginUserStore = useLoginUserStore()
const {historyList} = storeToRefs(historyStore)
const {list} = storeToRefs(recentCategory)
const {userId} = storeToRefs(loginUserStore)

const {categoryId} = defineProps({
    categoryId: {
        type: Number,
        required: true
    }
})
const isFirstTime = computed(() => isUndefined( find( historyList.value, o =>
        o.name == 'category_index' && o.params['id'] == route.params['id']
)))

const name = ref('')
const createTime = ref(0)
const avatar = ref('')
const info = ref('')
const rule = ref('')
const subscriberCount = ref(0)

const createDate = computed(() => {
    let date = new Date(createTime.value * 1000)
    return date.toISOString().split('T')[0]
})

watch(() => categoryId, () => {
    load()
}, {immediate: true})

async function load() {
    if (!isNumber(categoryId)) return
    let data = await getCategoryInfo(categoryId)
    name.value = data.name
    createTime.value = data.createTime
    avatar.value = data.avatar
    info.value = data.info
    rule.value = data.rule
    subscriberCount.value = data.subscriberCount
}

</script>

<style lang="scss" scoped>

</style>