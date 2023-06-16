import {NativeModules} from 'react-native';
const {CalendarModule} = NativeModules;

interface CalendarInterface {
  createCalendarEvent: (name: string, location: string) => void;
  getRandomNumber: (callback: (randomNumber: number) => void) => void;
}
export default CalendarModule as CalendarInterface;
