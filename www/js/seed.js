/* Offline fallback data (used only if babacosmetics.com cannot be reached on first launch).
   The app always tries to load LIVE products, prices & images from the store first. */
window.BC_SEED = (function () {
  const F = 'https://cdn.shopify.com/s/files/1/0632/0337/7241/files/';
  const rows = [
    // title | handle | type | price | compare | image
    ['Apple 24K Gold Ultra Glow Cream Bleach 500ml (Parlor Pack)','apple-24k-gold-ultra-glow-cream-bleach-500ml','bleach',1800,2500,'24k-Gold-Bleach-Creamm.webp?v=1771966693'],
    ['Vitamin C Bleach Cream Parlor Pack 500ml','vitamin-c-bleach-cream-parlor-pack-500ml','bleach',850,1000,'OrangeBleachCream_d3a20eab-f4f5-4f0e-93b0-ed618ab430eb.webp?v=1771966850'],
    ['Vitamin C Bleach Cream (5 Packs)','vitamin-c-bleach-creamparlor-pack','bleach',350,400,'Vitamin-C-Bleach-Cream_924e51f7-4b22-45a6-9465-0d9c4ee8d728.webp?v=1785999956'],
    ['Glutathione Bleach Cream (5 Packs)','glutathione-bleach-cream-5packs','bleach',400,450,'1_046e8437-2d7f-41da-bfe5-004ebddf9ad2.webp?v=1787042997'],
    ['Vitamin C Skin Serum','vitamin-c-skin-serum','Serum',700,800,'VitaminCSerum.webp?v=1783526118'],
    ['Pure Gluta Serum','pure-gluta-serum','Serum',700,800,'GlutathioneSerum.webp?v=1783574926'],
    ['Coffee Serum','coffee-serum','Serum',700,800,'CoffeeSerum.webp?v=1783574743'],
    ['Night Serum','night-skin-serum','Serum',700,800,'NightSerum.webp?v=1783574616'],
    ['Age Defying Serum','age-defying-serum','Serum',700,800,'AgeDefyingSerum.webp?v=1783574433'],
    ['Snail Skin Serum','snail-skin-serum','Serum',700,800,'SnailSerum.webp?v=1783574251'],
    ['Fresh Burst Vitamin C Facewash 75ml','fresh-brust-vitamin-c-facewash-75ml','Face Wash',300,350,'FreshBurstVitaminCFW.webp?v=1783525790'],
    ['Men Glow Detox Expert Facewash','men-glow-detox-expert-facewash-100ml','Face Wash',300,350,'DetoxExpertFW.webp?v=1781011380'],
    ['Men Glow Fairness Expert Facewash','men-glow-fairness-expert-facewash-100ml','Face Wash',300,350,'FairnessExpertFW.webp?v=1781011733'],
    ['Acne Control Facewash 75ml','acne-control-facewash-75ml','Face Wash',300,350,'AcneControlTeaTreeFW.webp?v=1777243791'],
    ['Glow on Rice Facewash 100ml','glow-on-rice-facewash-100ml','Face Wash',450,500,'GlowOnRiceFacewash.webp?v=1776159859'],
    ['Hydra Rice Facewash (Black Rice) 100ml','hydra-rice-facewash-black-rice-100ml','Face Wash',450,500,'HydraRiceFacewash.webp?v=1776158833'],
    ['Daily Herbal Neem Facewash','daily-herbal-neem-facewash','Face Wash',300,350,'NeemFacewash.webp?v=1770890298'],
    ['Activated Charcoal Facewash','activated-charcoal-facewash','Face Wash',300,350,'CharcoalFW_6e032b43-1136-49df-a81e-5ee4b92ec832.webp?v=1769637333'],
    ['Coffee Facewash (75ml)','coffee-facewash','Face Wash',300,350,'CoffeeFW_fd6a0d72-c055-4bf5-ba95-1230a293cf47.webp?v=1785999994'],
    ['9 in 1 Urgent Facial 150ml (Instant Glow)','9-in-1-urgent-facial-250ml-instant-glow','Facial',450,550,'9in1-Urgent-Facial.webp?v=1770888292'],
    ['9 in 1 Urgent Facial 50ml','9-in-1-urgent-facial-150-ml','Facial',450,500,'9in1-Urgent-Facial_b7f9cc9b-35ff-4cd3-8344-87a73253d612.webp?v=1785999969'],
    ['Glow on Rice & Coffee Urgent Facial 75ml','glow-on-rice-coffee-urgent-facial','Facial',300,350,'GlowOnRice_CoffeeUrgentFacial_73c9b218-d22b-458c-a9a3-87240dbdc707.webp?v=1776147960'],
    ['Glow On Rice & Coffee Facial Kit','glow-on-facial-kit','Facial',60,80,'Rice_CoffeeUrgentFacial.webp?v=1769770108'],
    ['Apricot Scrub 150ml','apricot-scrub-150-mlultra-glow','Scrub',400,450,'ApricotScrub.webp?v=1770890430'],
    ['Sun Block 100ml','sun-block-100ml','Sunblock',570,650,'SunBlock100ml.webp?v=1776160793'],
    ['Sun Block 50ml','sun-block','Sunblock',300,400,'SunBlock40ml.webp?v=1776160660'],
    ['Skin Polish 20ml','skin-polish-20ml','Skin Polish',100,120,'AppleGoldSkinPolish_4d572d93-ff38-41c7-b1c9-83a1a232dae3.webp?v=1785999981'],
    ['Hair Removal Cream Soft Shine Sachet','hair-removal-cream-soft-shine-sachet','Hair Removal',200,0,'HairremovalcreamSoftShine.webp?v=1783525654'],
    ['Beauty Glow Bundle','beauty-glow-bundle','bundle',1699,0,'Bundle-4_png.webp?v=1772179007'],
    ['BB Cream Bundle','bb-cream-bundle','bundle',899,0,'Bundle-3_png.webp?v=1772178476'],
    ['Eid Glow Bundle','eid-glow-bundle','bundle',1699,0,'Bundle-1_png.webp?v=1772178220'],
    ['Vitamin C Skin Care Bundle','vitamin-c-skin-care-bundle','bundle',1499,0,'Bundle-2_png.webp?v=1772178327'],
    ['Ultimate Coffee Glow Pack','ultimate-coffee-glow-pack','bundle',2499,0,'3_7b21340a-245d-4fac-8b8a-8c959dacc9b5.jpg?v=1785307672'],
    ['Complete Whitening & Polish Pack','complete-whitening-polish-pack','bundle',1899,0,'4_855bd775-1745-4e21-a7bd-b74f97c9c405.jpg?v=1785307795'],
    ["Men's Fairness & Care Pack",'mens-fairness-care-pack','bundle',1699,0,'2_b170a7ab-328e-41d2-9a4d-a2f2530dcd36.jpg?v=1785307414'],
    ['Azadi Glow Starter Pack','azadi-glow-starter-pack','bundle',999,0,'1_eca0d8d8-ca34-4ad4-ba74-9a878ce608c1.jpg?v=1785307240']
  ];
  const products = rows.map((r, i) => ({
    id: 9000 + i, handle: r[1], title: r[0], product_type: r[2], tags: [], body_html: '',
    images: [{ src: F + r[5] }],
    variants: [{ id: 0, title: 'Default Title', price: String(r[3]), compare_at_price: r[4] ? String(r[4]) : null, available: true }]
  }));
  return { products };
})();
