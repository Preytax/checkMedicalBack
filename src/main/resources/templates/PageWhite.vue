<template>
  <!-- ======= Top Bar ======= -->
  <Inc_top_bar/>
  <!-- ======= Header ======= -->
  <Inc_nav/>

  <main id="main">

    <!-- ======= Breadcrumbs Section ======= -->
    <section class="breadcrumbs">
      <div class="container">

        <div class="d-flex justify-content-between align-items-center">
          <h2>Inner Page</h2>
          <ol>
            <li><a href="index.html">Home</a></li>
            <li>Inner Page</li>
          </ol>
        </div>

      </div>
    </section><!-- End Breadcrumbs Section -->

    <section class="inner-page">
      <div class="container">
        <p>
          Example inner page template
        </p>
      </div>
    </section>

  </main><!-- End #main -->

  <div id="preloader" class="d-none"></div>
  <a href="#" class="back-to-top d-flex align-items-center justify-content-center"><i class="bi bi-arrow-up-short"></i></a>
</template>

<script>
  import Inc_nav from "./inc/Inc_nav";
  import Inc_top_bar from "./inc/Inc_top_bar";

  export default {
    //inject: ['BASE_URL'],
    name: "inc_head",
    components:{
      Inc_top_bar,
      Inc_nav
    },
    data() {
      return {
      };
    },
    methods: {
    },
    mounted() {
    }
  };
</script>