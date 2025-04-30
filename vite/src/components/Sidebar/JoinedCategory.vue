<template>
  <Suspense>
    <el-sub-menu index="joined-category">
      <!--用户加入的所有板块-->
      <template #title>
        <el-icon><View /></el-icon>
        <span>关注的板块</span>
      </template>
      <el-menu-item-group>
        <CategoryLink v-for="id in ids"
                      :key="id"
                      :category-id="id"/>
      </el-menu-item-group>
    </el-sub-menu>
  </Suspense>
</template>

<script setup>
import { getJoinedCategoryIds } from '@/js/api/user';

const {userId} = defineProps({
    userId: {
        required: true,
        type: Number
    }
})

const ids = ref([])

load()

async function load() {
    ids.value = await getJoinedCategoryIds(userId)
}

</script>

<style lang="scss" scoped>

</style>