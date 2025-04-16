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
import useRouteHistoryStore from '@/js/module/route_history';
import { find, initial, isEmpty, isUndefined } from 'lodash';
import { onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter()
const route = useRoute()

const recentCategory = useRecentCategoryStore()
const routeHistory = useRouteHistoryStore()

const {updateRecent} = recentCategory
const {historyList} = routeHistory

const id = ref(Number(route.params['id']))
const data = ref(await getPostDetails(id.value))
updateRecent(data.value.categoryId)

const isFirstTime = () => isUndefined( find( historyList.value, o =>
        o.name == 'post_detail' && o.params['id'] != route.params['id']
))

watch(route, async () => {
    if ( route.name == 'post_detail' )
        id.value = Number(route.params['id'])
    if (isFirstTime()) {
        data.value = await getPostDetails(id.value)
        updateRecent(data.value.categoryId)
    }
})

</script>

<style lang="scss" scoped>
.post-detailed-view {
    overflow-y: scroll;
    height: 100%;
}
</style>