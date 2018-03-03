/**
 * unix time
 * @param timestamp
 * @returns 12/30/2018 18:00 PM
 */
export function unix2CurrentTime(timestamp) {
  return new Date(timestamp).toLocaleString()
}
