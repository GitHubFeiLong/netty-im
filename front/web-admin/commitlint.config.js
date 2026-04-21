module.exports = {
  extends: ['@commitlint/config-conventional'],
  rules: {
    // ✅ 1. 关闭类型枚举检查（允许不写 feat/fix 等）
    'type-enum': [0, 'never', []],

    // ✅ 2. 关闭类型不能为空的检查（关键！解决 type-empty 报错）
    'type-empty': [0, 'never'],

    // ✅ 3. 关闭主题不能为空的检查（解决 subject-empty 报错）
    'subject-empty': [0, 'never'],

    'subject-full-stop': [0, 'never'],
    'subject-case': [0, 'never']
  }
}
