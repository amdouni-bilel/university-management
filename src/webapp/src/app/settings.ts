import { SideBarMenu } from './core/models';

export const subMenuItems: SideBarMenu[] = [,
  {
    code: '0',
    defaultLabel: 'Accueil',
    icon: 'grid',
    routerLink: ['/'],
    appAuthz: 'HOME',
  },
  {
    code: '196',
    defaultLabel: 'Statistique',
    icon: 'bar_chart',
    routerLink: ['/statistic'],
    appAuthz: 'HOME',
  },
  {
    code: '1',
    defaultLabel: 'Liste des utilisateurs',
    icon: 'users',
    routerLink: ['/users'],
    appAuthz: 'USERS',
  },
  {
    code: '44',
    defaultLabel: 'Les enseignants',
    icon: 'user_delete',
    routerLink: ['/teachers'],
    appAuthz: 'TEACHERS',
    subMenus: [
      {
        code: '26',
        defaultLabel: 'Liste des enseignants',
        routerLink: ['/teachers/list'],
        appAuthz: 'TEACHERSLIST'
      },
      {
        code: '27',
        defaultLabel: 'Indisponibilités',
        routerLink: ['/teachers/unavailability'],
        appAuthz: 'TEACHERSAVILABILITY'
      }
    ]
  },
  {
    code: '49',
    defaultLabel: 'Liste des étudiants',
    icon: 'user',
    routerLink: ['/students'],
    appAuthz: 'STUDENTS'
  },
  {
    code: '4',
    defaultLabel: 'Liste des classes',
    icon: 'form',
    routerLink: ['/classes'],
    appAuthz: 'CLASSES'
  },
  {
    code: '5',
    defaultLabel: 'Liste des modules',
    icon: 'copy',
    routerLink: ['/modules'],
    appAuthz: 'MODULES'
  },
  {
    code: '6',
    defaultLabel: 'Les salles',
    icon: 'home',
    routerLink: ['/rooms'],
    appAuthz: 'ROOMS',
    subMenus: [
      {
        code: '67',
        defaultLabel: 'Liste des salles',
        routerLink: ['/rooms/list'],
        appAuthz: 'ROOMSLIST'
      },
      {
        code: '66',
        defaultLabel: 'Indisponibilités',
        routerLink: ['/rooms/unavailability'],
        appAuthz: 'ROOMSAVILABILITY'
      }
    ]
  },
  {
    code: '7',
    defaultLabel: 'Liste des epreuves',
    icon: 'indent',
    routerLink: ['/exams'],
    appAuthz: 'EXAMS',
  },
  {
    code: '8',
    defaultLabel: 'Calendriers',
    icon: 'calendar',
    routerLink: ['/calendars'],
    appAuthz: 'CALENDARS',
    subMenus: [
      {
        code: '81',
        defaultLabel: 'Enseignants',
        routerLink: ['/calendars/teachers'],
        appAuthz: 'CALENDARTeacher'
      },
      {
        code: '82',
        defaultLabel: 'Classes',
        routerLink: ['/calendars/classes'],
        appAuthz: 'CALENDARClass'
      },
      {
        code: '84',
        defaultLabel: 'Salles',
        icon: 'calendar',
        routerLink: ['/calendars/rooms'],
        appAuthz: 'CALENDARRoom'
      },
      {
        code: '8',
        defaultLabel: 'Etudiants',
        routerLink: ['/calendars/students'],
        appAuthz: 'CALENDARSTUDENT'
      }
    ]
  }
];