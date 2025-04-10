<template>
    <el-container>
        <el-main>
            <PostList :size="15"/>
        </el-main>
        <el-aside class="info">

        </el-aside>
    </el-container>
</template>

<script setup>
import PostList from './Post/PostList.vue';


</script>

<style lang="scss" scoped>
.el-container {
    height: 100%;
}
.el-main {
    overflow: hidden;
}
</style>