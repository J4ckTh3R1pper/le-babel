<template>
    <el-card>
        <template #header>
            <el-text>板块详情</el-text>
        </template>
        <el-text>{{ info }}</el-text>
        <el-divider/>
        <el-text>{{ '版规: ' + rule }}</el-text>
        <el-divider/>
        <el-text>{{ '创建时间: ' + createDate }}</el-text>
        <el-divider/>
        <el-text>{{ '关注: ' + subscriberCount }}</el-text>
    </el-card>
</template>

<script setup>
import { getCategoryInfo } from '@/js/api/category';
import useRouteHistoryStore from '@/js/module/route_history';
import { find, isUndefined } from 'lodash';
import { storeToRefs } from 'pinia';
import { reactive } from 'vue';
import { routeLocationKey } from 'vue-router';
import Time from '../Time.vue';
import useRecentPostStore from '@/js/module/recent_post';
import mitt from 'mitt';
import useRecentCategoryStore from '@/js/module/recent_category';
const route = useRoute()
const historyStore = useRouteHistoryStore()
const recentPost = useRecentPostStore()
const recentCategory = useRecentCategoryStore()
const {historyList} = storeToRefs(historyStore)
const {list} = storeToRefs(recentCategory)
const categoryId = ref(null)
const isFirstTime = computed(() => isUndefined( find( historyList.value, o =>
        o.name == 'category_index' && o.params['id'] == route.params['id']
)))

const name = ref('')
const emitter = mitt()
const createTime = ref(0)
const avatar = ref('')
const info = ref('')
const rule = ref('')
const subscriberCount = ref(0)

const createDate = computed(() => {
    let date = new Date(createTime.value * 1000)
    return date.toISOString().split('T')[0]
})

watch(route, () => {
    if ( route.name == 'category_index' ) {
        categoryId.value = Number(route.params['id'])
    }
}, {immediate: true})

watch(categoryId, () => {
    if (categoryId.value && isFirstTime.value) load()
}, {immediate: true})

watch(list, () => {
    categoryId.value = list.value[0]
})

async function load() {
    let data = await getCategoryInfo(categoryId.value)
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