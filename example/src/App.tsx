import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import PDFLib from '@kamo/react-native-pdf-lib';

export default function App() {
  // const [result, setResult] = React.useState<number | undefined>();

  // console.log(PDFLib);

  return (
    <View style={styles.container}>{/* <Text>Result: {result}</Text> */}</View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
