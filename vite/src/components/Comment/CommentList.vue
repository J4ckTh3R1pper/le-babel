<template>
    <div class="comment-list"
            v-infinite-scroll="load"
            :infinite-scroll-delay="500"
            infinite-scroll-immediate
    >
        <el-select v-model="sort">
            <el-option v-for="i in sortings"
                :value="i.value"
                :label="i.label"
                :key="i.label"
            />
        </el-select>
        <ul class="list">
            <li v-for="i in list">
                <CommentBriefView class="root-comment"
                    :data="i"
                />
            </li>
        </ul>
    </div>
</template>

<script setup>
import { getCommentByPostId } from '@/js/api/comment';
import CommentBriefView from './CommentBriefView.vue';
import { isEmpty, uniqBy } from 'lodash';

const {postId, size = 15} = defineProps({
    postId: Number,
    size: Number
})

const sortings = [
    {
        label: '按时间倒序',
        value: 'id,desc'
    }, {
        label: '按时间顺序',
        value: 'id,asc'
    }
]

const page = ref(0)
const sort = ref(sortings[0].value)
const list = ref([])
const loading = ref(true)
const emit = defineEmits(['finish-load'])

async function load(reload) {
    if (!reload) page.value += 1
    loading.value = true
    let data;
    try {
        data = await getCommentByPostId(postId, page.value, size, sort.value)
        // console.log(data)
        if (!data['empty']) {
            list.value = uniqBy(list.value.concat(data.content), 'id')
    }
    } catch (error) {
        console.log(error)
    } finally {
        loading.value = false
    }
}

watch(sort, async () => {
    list.value = []
    page.value = 0
    load()
}, {} )

load()

defineExpose({
    load
})

</script>

<style lang="scss">
.comment-list {
    height: 100%;
    overflow: scroll;
    padding-bottom: 0;
}
.list {
    overflow-y: visible;
    list-style: none;
    padding: 10px 0;
    margin: 0;
}
.root-comment {
    & > details {
        background: none !important;
        & > summary {
            background: none !important;
        }
    }
}
</style>