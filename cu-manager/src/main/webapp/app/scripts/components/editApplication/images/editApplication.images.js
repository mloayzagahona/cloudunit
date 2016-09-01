/*
 * LICENCE : CloudUnit is available under the Affero Gnu Public License GPL V3 : https://www.gnu.org/licenses/agpl-3.0.html
 *     but CloudUnit is licensed too under a standard commercial license.
 *     Please contact our sales team if you would like to discuss the specifics of our Enterprise license.
 *     If you are not sure whether the GPL is right for you,
 *     you can always test our software under the GPL and inspect the source code before you contact us
 *     about purchasing a commercial license.
 *
 *     LEGAL TERMS : "CloudUnit" is a registered trademark of Treeptik and can't be used to endorse
 *     or promote products derived from this project without prior written permission from Treeptik.
 *     Products or services derived from this software may not be called "CloudUnit"
 *     nor may "Treeptik" or similar confusing terms appear in their names without prior written permission.
 *     For any questions, contact us : contact@treeptik.fr
 */

(function () {
  'use strict';
  angular
    .module ( 'webuiApp.editApplication' )
    .component ( 'editAppModules', Modules() );

  function Modules () {
    return {
      templateUrl: 'scripts/components/editApplication/images/editApplication.images.html',
      bindings: {
        app: '='
      },
      controller: [
        '$rootScope',
        'ImageService',
        'ModuleService',
        '$stateParams',
        ModulesCtrl
      ],
      controllerAs: 'modules',
    };
  }

  function ModulesCtrl ( $rootScope, ImageService, ModuleService, $stateParams) {
    var vm = this;
    vm.moduleImages = [];
    vm.categorieImage = [];
    vm.addModule = addModule;
    vm.typeImage = $stateParams.typeImage;
    
    vm.$onInit = function() {
      getModulesImages ();
    }
    
    function getModulesImages () {
      return ImageService.findEnabledModule ()
        .then ( success )
        .catch ( error );

      function success ( images ) {
        var imagesPicked;
        var listingCategorie =  [];

        // isTemp Key for gray card
        vm.moduleImages = images;
        vm.moduleImages.push({
          id: 9999,
          name: "influxdb",
          path: "cloudunit/influxdb",
          displayName: "Influxdb",
          status: null,
          isTemp: true
        });
        
        vm.moduleImages.push({
          id: 9998,
          name: "mongo-3-2",
          path: "cloudunit/mongo-3-2",
          displayName: "Mongo 3.2",
          status: null,
          isTemp: true
        });

        if(vm.typeImage === '') {
          // @TODO foreach
          for(var i=0 ; i < vm.moduleImages.length - 1 ; i++) {
            if (!(vm.categorieImage.indexOf(vm.moduleImages[i].prefixEnv) != -1 || vm.moduleImages[i].prefixEnv == undefined)) {
              vm.categorieImage.push(vm.moduleImages[i].prefixEnv);
            }
          }
        }
        
        return vm.moduleImages;
      }

      function error () {
        console.log ( 'cannot get modules images' );
      }
    }

    // Ajout d'un module
    function addModule ( applicationName, imageName ) {
      // @TODO redirection to overview
      ModuleService.addModule ( applicationName, imageName ).then(function (data) {
        $rootScope.$broadcast('application:addModule');
        return data;
      } );
    }
  }
}) ();
