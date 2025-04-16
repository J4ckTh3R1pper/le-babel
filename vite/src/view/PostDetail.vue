<template>
    <BackButton :category-id="data.categoryId"/>
    <PostDetailedView
        :post-id="data.id"
        :user-id="data.userId"
        :title="data.title"
        :category-id="data.categoryId"
        :comment-count="data.commentCount"
        :content="data.content"
        :create-time="data.createTime"
        :tags="data.tags"
        :like-count="data.likeCount"
        :view-count="data.viewCount"
        :liked="data.liked"
        :last-update-time="data.lastUpdateTime"
    />
</template>

<script setup>
import BackButton from '@/components/Category/BackButton.vue';
import PostDetailedView from '@/components/Post/PostDetailedView.vue';
import { getPostDetails } from '@/js/api/post';
import useRecentCategoryStore from '@/js/module/recent_category';
import useRecentPostStore from '@/js/module/recent_post';
import useRouteHistoryStore from '@/js/module/route_history';
import { find, initial, isEmpty, isUndefined } from 'lodash';
import { storeToRefs } from 'pinia';
import { onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter()
const route = useRoute()

const recentCategory = useRecentCategoryStore()
const routeHistory = useRouteHistoryStore()
const recentPost = useRecentPostStore()

const {historyList} = storeToRefs(routeHistory)

const id = ref(Number(route.params['id']))
const data = ref(await getPostDetails(id.value))
recentCategory.updateRecent(data.value.categoryId)
recentPost.updateRecent(id.value)

const isFirstTime = computed(() => isUndefined( find( historyList.value, o =>
        o.name == 'post_detail' && o.params['id'] == id.value
)))

watch(route, async (newRoute) => {
    if ( newRoute.name == 'post_detail' ) {
        console.log(isFirstTime.value)
        id.value = Number(newRoute.params['id'])
        if (isFirstTime.value) {
            console.log(isFirstTime.value)
            data.value = await getPostDetails(id.value)
            recentCategory.updateRecent(data.value.categoryId)
            recentPost.updateRecent(id.value)
        }
    }
})

</script>

<style lang="scss" scoped>
.post-detailed-view {
    overflow-y: scroll;
    height: 100%;
}
</style>