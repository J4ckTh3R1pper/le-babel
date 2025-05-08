<template>
<div class="post-list"
               v-infinite-scroll="load"
               :infinite-scroll-delay="500"
               :infinite-scroll-immediate="false"
>
    <el-select v-model="sort" class="sort">
        <el-option v-for="i in sortings"
            :value="i.value"
            :label="i.label"
            :key="i.label"
        />
    </el-select>
    <ul class="list"
    >
        <li v-for="i in list" :key="i.id">
            <PostBriefView
                    :post-id="i.id"
                    :title="i.title"
                    :user-id="i.userId"
                    :category-id="i.categoryId"
                    :content="i.content"
                    :create-time="i.createTime"
                    :last-update-time="i.lastUpdateTime"
                    :tags="i.tags"
                    :view-count="i.viewCount"
                    :like-count="i.likeCount"
                    :comment-count="i.commentCount"
                    :thumbnails="i.thumbnails"
                    :liked="i.liked"
                    :show-category="showCategory"
                    :show-user="!isNumber(userId)"
            />
        </li>
        <li>
            <div class="loading" v-loading="loading">
                <span class="empty">已经到底了</span>
            </div>
        </li>
        <el-divider/>
    </ul>
</div>
</template>

<script setup>
import { getPostSlice } from '@/js/api/post';
import PostBriefView from './PostBriefView.vue';
import { onMounted, watchEffect } from 'vue';
import { isNumber } from 'lodash';
const sortings = [
    {
        label: '最新回复',
        value: 'lastUpdateTime'

    },
    {
        label: '最新发布',
        value: 'createTime'
    },
    {
        label: '最多回复',
        value: 'commentCount'
    },

]
const sort = ref(sortings[0].value)
const page = ref(1)
const {categoryId = null, userId = null, size = 15, showCategory} = defineProps({
    categoryId: Number,
    userId: Number,
    size: Number,
    showCategory: Boolean
})
const disabled = ref(false)
const list = ref([])
const loading = ref(false)
const lastPage = ref(false)
const emit = defineEmits(['finish-load'])

watch(sort, async () => {
    reload()
}, {} )

function reload() {
    list.value = []
    page.value = 1
    load()
}

watch(() => categoryId, async () => {
    reload()
}, { immediate: true, deep: true})

watch(loading, () => {
    if (!loading.value) emit('finish-load')
})

function load() {
    loading.value = true
    getPostSlice(categoryId, userId, page.value, size, sort.value+',desc').then(res => {
        if (!res["empty"]) {
            list.value = list.value.concat(res.content)
            page.value += 1
            lastPage.value = false
            // console.log(res)
        } else lastPage.value = true
    }).catch(() => {
        disabled.value = true
    })
    .finally( () => {
        loading.value = false
    })
}

</script>

<style scoped lang="scss">
.sort {
    margin-bottom: 1em;
}

.post-list {
    height: 100%;
    overflow: scroll;
    padding-bottom: 0;
}
.list {
    overflow-y: visible;
    list-style: none;
    padding: 0;
    margin: 0;
}

.loading {
    height: 5em;
    display: flex;
    justify-content: center;
    .empty {
        color: gray;
        padding-top: 1em;
    }
}
</style>