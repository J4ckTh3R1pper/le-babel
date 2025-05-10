<template>
<div class="search-bar">
    <el-input class="input" v-model="keyword" @keyup.enter="jumpToSearchPage">
        <template #prefix>
            <el-icon><Search /></el-icon>
        </template>
    </el-input>
    <el-button @click="jumpToSearchPage">搜索</el-button>
</div>
</template>

<script setup>
import { isEmpty } from 'lodash';
import { onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute()
const router = useRouter()
const keyword = ref('')

function setKeywordByURL() {
    if (route.name == 'search_post') keyword.value = route.params['keyword']
}

onMounted(() => {
    setKeywordByURL()
})

watch(route, () => setKeywordByURL())

function jumpToSearchPage() {
    if (isEmpty(keyword.value)) return
    router.push({
        name: 'search_post',
        params: {
            keyword: keyword.value
        }
    })
}

</script>

<style lang="scss" scoped>
.search-bar {
    display: flex;
    align-items: center;
}
</style>