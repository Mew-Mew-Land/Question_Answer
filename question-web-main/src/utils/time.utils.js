export const transformIsoDate = (isoString) => {
  const date = new Date(isoString);
  const localDateString = date.toLocaleString("zh-CN", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  });
  return localDateString;
};

export const currentTime = () => {
  const time = new Date();
  const hour = time.getHours();
  if (hour < 12) {
    return "早上";
  } else if (hour > 12 && hour < 14) {
    return "中午";
  } else if (hour > 14 && hour < 18) {
    return "下午";
  } else if (hour > 18) {
    return "晚上";
  }
};
