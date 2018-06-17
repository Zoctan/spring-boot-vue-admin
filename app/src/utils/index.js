/**
 * Unix时间戳转换成日期格式  unix2CurrentTime("1497232433000")
 * @param unixTime Unix时间戳
 * @return string yyyy-MM-dd HH:mm:ss
 */
export function unix2CurrentTime(unixTime) {
  const date = new Date(parseInt(unixTime))
  const y = date.getFullYear()
  let m = date.getMonth() + 1
  m = m < 10 ? ('0' + m) : m
  let d = date.getDate()
  d = d < 10 ? ('0' + d) : d
  let h = date.getHours()
  h = h < 10 ? ('0' + h) : h
  let minute = date.getMinutes()
  let second = date.getSeconds()
  minute = minute < 10 ? ('0' + minute) : minute
  second = second < 10 ? ('0' + second) : second
  return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second
}

/**
 * 两个Unix时间戳差值
 * @param unixTimeStart Unix时间戳1
 * @param unixTimeEnd Unix时间戳2
 * @return string xx 小时 | xx 天
 */
export function unixDifference(unixTimeStart, unixTimeEnd) {
  const difference = (unixTimeEnd - unixTimeStart) / 1000
  if (difference >= 86400) {
    return difference / 86400 + '天'
  } else if (difference >= 3600) {
    return difference / 3600 + '小时'
  } else if (difference >= 60) {
    return difference / 60 + '分钟'
  } else {
    return difference + '秒'
  }
}

/**
 * 当前Unix时间戳差值
 * @param unixTimeEnd Unix时间戳
 * @return string | null xx天xx小时xx分钟xx秒
 */
export function nowDifference(unixTimeEnd) {
  const unixTimeStart = new Date().getTime()
  const difference = (unixTimeEnd - unixTimeStart) / 1000
  if (difference > 0) {
    let day = Math.floor(difference / (60 * 60 * 24))
    let hour = Math.floor(difference / (60 * 60)) - (day * 24)
    let minute = Math.floor(difference / 60) - (day * 24 * 60) - (hour * 60)
    let second = Math.floor(difference) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60)
    if (day <= 9) day = '0' + day
    if (hour <= 9) hour = '0' + hour
    if (minute <= 9) minute = '0' + minute
    if (second <= 9) second = '0' + second
    return day + '天' + hour + '小时' + minute + '分钟' + second + '秒'
  } else {
    return null
  }
}
