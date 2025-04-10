<script setup>
import {ref, computed} from 'vue'

</script>

<script>

import {createMemoryHistory, createRouter} from "vue-router";
import {constantRoutes} from "@/router/index.js";

</script>

<template>
  <router-view/>
</template>

<style>
body {
  font-family: 'Roboto';
}
</style>
