<script setup lang="ts">
import { unref, reactive } from 'vue'
import { ContentWrap } from '@/components/ContentWrap'
import { useI18n } from '@/hooks/web/useI18n'
import { useTable } from '@/hooks/web/useTable'
import { ElTag } from 'element-plus'
import { Table } from '@/components/Table'
const { t } = useI18n()
import { BaseButton } from '@/components/Button'
import { pageApi } from '@/api/sysUser'
import { CrudSchema, useCrudSchemas } from '@/hooks/web/useCrudSchemas'

const save = (show: boolean) => {
  console.log('hello world', show)
}

// @ts-ignore
const { allSchemas } = useCrudSchemas(crudSchemas)

const { tableRegister, tableMethods, tableState } = useTable({
  fetchDataApi: async () => {
    const { currentPage, pageSize } = tableState
    const res = await pageApi({
      page: unref(currentPage),
      size: unref(pageSize)
    })
    return {
      list: res.data.records,
      total: res.data.total
    }
  }
})

const { loading, dataList, total, currentPage, pageSize } = tableState

const crudSchemas = reactive<CrudSchema[]>([
  {
    field: 'selection',
    search: {
      hidden: true
    },
    form: {
      hidden: true
    },
    detail: {
      hidden: true
    },
    table: {
      type: 'selection'
    }
  },
  {
    field: 'index',
    label: t('tableDemo.index'),
    type: 'index',
    search: {
      hidden: true
    },
    form: {
      hidden: true
    },
    detail: {
      hidden: true
    }
  },
  {
    field: 'title',
    label: t('tableDemo.title'),
    search: {
      component: 'Input'
    },
    form: {
      component: 'Input',
      colProps: {
        span: 24
      }
    },
    detail: {
      span: 24
    }
  },
  {
    field: 'author',
    label: t('tableDemo.author'),
    search: {
      hidden: true
    }
  },
  {
    field: 'display_time',
    label: t('tableDemo.displayTime'),
    search: {
      hidden: true
    },
    form: {
      component: 'DatePicker',
      componentProps: {
        type: 'datetime',
        valueFormat: 'YYYY-MM-DD HH:mm:ss'
      }
    }
  },
  {
    field: 'importance',
    label: t('tableDemo.importance'),
    search: {
      hidden: true
    },
    form: {
      component: 'Select',
      componentProps: {
        style: {
          width: '100%'
        },
        options: [
          {
            label: '重要',
            value: 3
          },
          {
            label: '良好',
            value: 2
          },
          {
            label: '一般',
            value: 1
          }
        ]
      }
    }
  },
  {
    field: 'pageviews',
    label: t('tableDemo.pageviews'),
    search: {
      hidden: true
    },
    form: {
      component: 'InputNumber',
      value: 0
    }
  },
  {
    field: 'content',
    label: t('exampleDemo.content'),
    search: {
      hidden: true
    },
    table: {
      show: false
    },
    form: {
      component: 'Editor',
      colProps: {
        span: 24
      }
    },
    detail: {
      span: 24,
      slots: {
        default: (data: any) => {
          return <div innerHTML={data.content}></div>
        }
      }
    }
  },
  {
    field: 'action',
    width: '260px',
    label: t('tableDemo.action'),
    search: {
      hidden: true
    },
    form: {
      hidden: true
    },
    detail: {
      hidden: true
    }
  }
])

// @ts-ignore
const { allSchemas } = useCrudSchemas(crudSchemas)
</script>

<template>
  <ContentWrap title="SysUser">
    <BaseButton @click="save(true)">
      {{ t('tableDemo.show') }} {{ t('tableDemo.pagination') }}
    </BaseButton>
    <Table
      v-model:pageSize="pageSize"
      v-model:currentPage="currentPage"
      showAction
      showSummary
      default-expand-all
      :columns="allSchemas.tableColumns"
      row-key="id"
      :data="dataList"
      :loading="loading"
      :pagination="{
        total: total
      }"
      @register="tableRegister"
    />
    <!-- @refresh="refresh" -->
  </ContentWrap>
</template>
