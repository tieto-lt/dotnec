var module = require('main_module');

function Controller(missionService) {
  var vm = this;

  vm.oneAtATime = true;

  vm.status = {
    isCustomHeaderOpen: false,
    isFirstOpen: true,
    isFirstDisabled: false
  };


  vm.loadDetails = function loadDetails(element){

    // slides
    if(element.status && element.status.open){
      return;
    }
    vm.navigationData = {
      startX: undefined,
      startY: undefined,
      endX: undefined,
      endY: undefined,
      battery: undefined
    };

    vm.noWrapSlides = false;
    vm.active = 0;
    var slides = vm.slides = [];
    var currIndex = 0;

    vm.addSlide = function(image) {
      slides.push({
        image: image,
        id: currIndex++
      });
    };

    vm.randomize = function() {
      var indexes = generateIndexesArray();
      assignNewIndexesToSlides(indexes);
    };

    missionService.retrieveMissionDetails(vm.mission.id).then(function(data){
      var images = data.data.images;
      var navigationData = data.data.navigationData;
      if(navigationData){
        vm.navigationData.startX = navigationData[0].x;
        vm.navigationData.startY = navigationData[0].y;
        vm.navigationData.endX = navigationData[navigationData.length-1].x;
        vm.navigationData.endY = navigationData[navigationData.length-1].y;
        vm.navigationData.battery = navigationData[navigationData.length-1].battery;
        vm.prepareBar(vm.navigationData.battery);
      }

      for(i = 0; i<images.length; i++){
        vm.addSlide(images[i].imageBase64);
      }

    });

    // Randomize logic below

    function assignNewIndexesToSlides(indexes) {
      for (var i = 0, l = slides.length; i < l; i++) {
        slides[i].id = indexes.pop();
      }
    }

    function generateIndexesArray() {
      var indexes = [];
      for (var i = 0; i < currIndex; ++i) {
        indexes[i] = i;
      }
      return shuffle(indexes);
    }

    // http://stackoverflow.com/questions/962802#962890
    function shuffle(array) {
      var tmp, current, top = array.length;

      if (top) {
        while (--top) {
          current = Math.floor(Math.random() * (top + 1));
          tmp = array[current];
          array[current] = array[top];
          array[top] = tmp;
        }
      }

      return array;
    }

    vm.max = 200;


   //Progress bar

    vm.prepareBar = function(battery) {
      var value = battery;
      var type;

      if (value < 25) {
        type = 'danger';
      } else if (value < 50) {
        type = 'warning';
      } else if (value < 75) {
        type = 'info';
      } else {
        type = 'success';
      }
      vm.dynamic = value;
      vm.type = type;
    };



  };


}

Controller.$inject = ['MissionService'];

module.component('missionDetailsComponent', {
    controller: Controller,
    templateUrl: require('./missionDetailsComponent.html'),
    bindings: {
      mission: '='
    }
});