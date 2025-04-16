<template>
    <el-button @click="onClicked" round icon="ArrowLeft"><el-avatar :src="data.avatar" size="small"/>{{ data.name }}</el-button>
</template>

<script setup>
import useCategoryCacheStore from '@/js/module/category_cache';
import { useRouter } from 'vue-router';

const router = useRouter()

const store = useCategoryCacheStore()

const {categoryId} = defineProps({
  categoryId: {
    type: Number,
    required: true
  },
})

const data = await store.fetchCategory(categoryId)

function onClicked() {
    router.push({
        name: 'category_index',
        params: {
            id: categoryId
        }
    })
}

</script>

<style lang="scss" scoped>
.el-avatar {
    margin-right: 6px;
}
</style>