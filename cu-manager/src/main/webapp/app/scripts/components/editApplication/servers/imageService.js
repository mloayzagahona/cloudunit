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

  /**
   * @ngdoc service
   * @name webuiApp.imagesservice
   * @description
   * # imagesservice
   * Factory in the webuiApp.
   */
  angular
    .module('webuiApp')
    .factory('ImageService', ImageService);

  ImageService.$inject = ['$resource'];


  function ImageService($resource) {

    return {
      findEnabled: findEnabled,
      findEnabledServer: findEnabledServer,
      findEnabledModule: findEnabledModule,
      findAll: findAll
    };


    //////////////////////////////////////////

    // Liste de toutes les images qui sont activés quelque soit leur type
    function findEnabled() {
      var list = $resource('image/enabled');
      return list.query();
    }

    // Liste de toutes les images de type server
    function findEnabledServer() {
      var list = $resource('image/server/enabled');
      return list.query().$promise;
    }

    // Liste de toutes les images de type module
    function findEnabledModule() {
      var list = $resource('image/module/enabled');
      return list.query().$promise;
    }

    // Liste de toutes les images
    function findAll() {
      var listImages = $resource('image/all');
      return listImages.query();
    }
  }

})();

