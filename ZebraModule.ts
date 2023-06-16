import {NativeModules} from 'react-native';
const {ZebraModule} = NativeModules;

interface ZebraInterface {
  initBarcodeScanner: () => void;
}
export default ZebraModule as ZebraInterface;
