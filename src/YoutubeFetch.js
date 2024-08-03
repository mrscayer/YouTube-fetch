import { NativeModules } from 'react-native';

const { YoutubeFetch } = NativeModules;

export const fetchData = (url, headers, body) => {
  return YoutubeFetch.fetchData(url, headers, body);
};
