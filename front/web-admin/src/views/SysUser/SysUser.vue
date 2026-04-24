<script setup lang="ts">
import { ContentWrap } from '@/components/ContentWrap'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()
import { BaseButton } from '@/components/Button'
import { pageApi } from '@/api/sysUser'

const save = (show: boolean) => {
  console.log('hello world', show)
}

pageApi({})

// const { tableRegister, tableMethods, tableState } = useTable({
//   fetchDataApi: async () => {
//     const { currentPage, pageSize } = tableState
//     const res = await pageApi({
//       pageIndex: unref(currentPage),
//       pageSize: unref(pageSize)
//     })
//     return {
//       list: res.data.list,
//       total: res.data.total
//     }
//   }
// })
</script>

<template>
  <ContentWrap title="SysUser">
    <BaseButton @click="save(true)">
      {{ t('tableDemo.show') }} {{ t('tableDemo.pagination') }}
    </BaseButton>
    <!-- <Table
      v-model:pageSize="pageSize"
      v-model:currentPage="currentPage"
      showAction
      showSummary
      default-expand-all
      :columns="columns"
      row-key="id"
      :data="dataList"
      :loading="loading"
      :pagination="
        canShowPagination
          ? {
              total: total
            }
          : undefined
      "
      @register="tableRegister"
      @refresh="refresh"
    /> -->
  </ContentWrap>
</template>
