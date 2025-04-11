<script setup>

import {Clock} from "@element-plus/icons-vue";

const { timestamp } = defineProps({
  timestamp: {
    type: Number,
    required: true
  }
})

const timeString = ref('')
watchEffect(() => {
  let date = new Date(timestamp * 1000)
  timeString.value = Date.now() - timestamp < 86400000 ? date.toLocaleTimeString() : date.toLocaleString()
})

</script>

<template>
<span class="time">
  <el-icon><Clock /></el-icon>
  {{timeString}}
</span>
</template>

<style scoped lang="scss">
.time {
  display: flex;
  align-items: inherit;
  .el-icon {
    margin-right: 4px;
  }
}
</style>